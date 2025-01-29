<script>

    import Router from "svelte-spa-router";
    import LoginPage from "./pages/LoginPage.svelte";
    import CreateAccount from "./pages/CreateAccount.svelte";
    import HomePage from "./pages/HomePage.svelte";
    import AccuntDetails from "./pages/AccountDetails.svelte";
    import { checkLoginStatus } from "./stores/stores";
    import { onMount } from "svelte";
    const routes = {
        '/' : LoginPage,
        '/CreateAccount' : CreateAccount,
        '/home' : HomePage,
        '/account-details' : AccuntDetails
    };

    let isLoggedIn = false;

    onMount(async () => {
        isLoggedIn = await checkLoginStatus();
        console.log("On mount login check");
    });
</script>

<main>
    <Router { routes } />
</main>
