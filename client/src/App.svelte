<script>

    import Router from "svelte-spa-router";
    import LoginPage from "./pages/LoginPage.svelte";
    import CreateAccount from "./pages/CreateAccount.svelte";
    import HomePage from "./pages/HomePage.svelte";


    const routes = {
        '/' : LoginPage,
        '/CreateAccount' : CreateAccount,
        '/home' : HomePage
    };


</script>

<main>
    <Router { routes } />
</main>

<style></style>
