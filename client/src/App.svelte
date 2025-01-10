<script>

    import Router from "svelte-spa-router";
    import LoginPage from "./pages/LoginPage.svelte";
    import CreateAccount from "./pages/CreateAccount.svelte";
    import HomePage from "./pages/HomePage.svelte";
    import DevelopmentPage from "./pages/DevelopmentPage.svelte";

    const routes = {
        '/dev' : DevelopmentPage,
        '/' : LoginPage,
        '/CreateAccount' : CreateAccount,
        '/home' : HomePage
    };


</script>

<main>
    <Router { routes } />
</main>
