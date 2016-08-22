import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CompletableFutureDemo implements PrintableTest {

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2);

    private List<Map<String,Object>> parse(String s) {
        final Type typeOf = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(s, typeOf);
    }

    private List<Map<String,Object>> parseRepo(String s) {
        final Type typeOf = new TypeToken<List<Map<String,Object>>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(s, typeOf);
    }

    private List<String> extractNameFromRepos(List<Map<String, Object>> repos) {
        return repos.stream().map(r -> (String)r.get("name")).collect(Collectors.toList());
    }





    public CompletableFuture<List<String>> getReposForUser(String user) {
        final OkHttpClient client = new OkHttpClient();
        String url = Arrays.asList("https://api.github.com/users", user, "repos")
                .stream().collect(Collectors.joining("/"));
        return callGitHub(client, url)
                .thenApply(this::parse)
                .thenApply(this::extractNameFromRepos);

    }

    public CompletableFuture<List<String>> getBranchesForUser(String user, String repo) {
        final OkHttpClient client = new OkHttpClient();
        String url = Arrays.asList("https://api.github.com/repos", user, repo, "branches")
                .stream().collect(Collectors.joining("/"));
        return callGitHub(client, url)
                .thenApply(this::parseRepo)
                .thenApply(this::extractNameFromRepos);
    }

    public CompletableFuture<String> callGitHub(OkHttpClient client, String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Network call on: " + Thread.currentThread().getName());
                final Request request = new Request.Builder()
                        .url(url)
                        .build();
                return client.newCall(request).execute().body().string();
            }catch(Exception e) {
                System.err.println(e);
                return null;
            }
        }, executorService);
    }



    @Test
    public void simpleCF() throws Exception {
        /**
         * To create an completable future, we can use its builder methods
         * runAsync, supplyAsync
         */

        CompletableFuture
                .supplyAsync(() -> Thread.currentThread().getName())
                .thenAccept(this::print).get();

    }

    @Test
    public void composedOperations() throws Exception {
        /**
         * Here we have a cascade of operations that all are happening in the
         * caller thread
         */
        getReposForUser("eginez")
                .thenAccept(this::print)
                .get();
    }

    @Test
    public void composedAsync() throws Exception {
        /**
         * But what if we had to call multiple services to gather information.
         * cascading service calls can be performed with thenCompose without
         * having to worry about callback hell
         */
        getReposForUser("eginez")
                //.thenAccept                                                     //this nests calls
                //.thenApply(names -> getBranchesForUser("eginez", names.get(0))) //this returns a CompletableFuture<CompleteableFuture<>>
                .thenCompose(names -> getBranchesForUser("eginez", names.get(0)))
                .thenAccept(this::print)
                .get();
    }

    @Test
    public void combine() throws Exception {
        /**
         * let's say I need to combine the reponses from two different services
         */

        CompletableFuture<List<String>> reposOfBrandur = getReposForUser("brandur");
        CompletableFuture<List<String>> myRepos = getReposForUser("eginez");
        CompletableFuture<Void> combined = myRepos.thenCombineAsync(reposOfBrandur, (r1, r2) -> {
            r1.addAll(r2);
            return r1;
        }).thenAccept(this::print);

        combined.get();
    }

    @Test
    public void any() throws Exception {
        /**
         * Or let's say I have to endpoints with the same information, and I only want to use one of them
         */

        CompletableFuture<List<String>> reposOfBrandur = getReposForUser("brandur");
        CompletableFuture<List<String>> myRepos = getReposForUser("eginez");
        CompletableFuture<Void> firstOne = myRepos.acceptEither(reposOfBrandur, this::print);
        firstOne.get();
    }


    @Test
    public void allOf() throws Exception {

        /**
         * Returns the reponse of multiple calls
         */
        //List of server calls
        CompletableFuture<List<String>> reposOfBrandur = getReposForUser("brandur");
        CompletableFuture<List<String>> myRepos = getReposForUser("eginez");


        CompletableFuture<Void> all = CompletableFuture.allOf(reposOfBrandur, myRepos)
                .thenAccept((v) -> {
                    try {
                        //This will not block since AllOf gurantees the futures to be complete
                        print(reposOfBrandur.get());
                        print(myRepos.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        all.get();
    }

    @Test
    public void anyOf() throws Exception {

        /**
         * Returns the reponse of multiple calls
         */
        //List of server calls
        CompletableFuture<List<String>> reposOfBrandur = getReposForUser("brandur");
        CompletableFuture<List<String>> myRepos = getReposForUser("eginez");


        CompletableFuture<Void> any = CompletableFuture.anyOf(reposOfBrandur, myRepos)
                .thenAccept(this::print);
        any.get();
    }


    @Test
    public void manualFutures() throws Exception {
        /**
         * CompletableFutures can be completed arbitrarily from any thread
         */
        final CompletableFuture<List<String>> cachedCall = new CompletableFuture<>();
        pool.schedule(
                () -> cachedCall.complete(Arrays.asList("repo1", "repo2")),
                100, TimeUnit.MILLISECONDS);

        CompletableFuture<Void> repos = CompletableFuture.anyOf(getReposForUser("eginez"), cachedCall)
                .thenAccept(this::print);
        repos.get();
    }



    public CompletableFuture<List<String>> getNotifier(ExecutorService thread) {
        final CompletableFuture<List<String>> notifier = new CompletableFuture<>();

        final CompletableFuture<Void> action =
                getReposForUser("eginez")
                        .thenAccept(notifier::complete);

        thread.submit (() -> {
            try {
                Thread.sleep(1000);
                action.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return notifier;

    }

    @Test
    public void pushNotifications() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CompletableFuture<List<String>> notifier = getNotifier(pool);

        pool.submit(() -> {
            try {
                System.out.println("waiting on thread:" + Thread.currentThread().getName());
                print(notifier.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        print("Main thread: Waiting to terminate");
        pool.awaitTermination(2000, TimeUnit.MILLISECONDS);

    }



}
