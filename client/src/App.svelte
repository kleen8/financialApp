<script>

    import Router, { push } from "svelte-spa-router";
    import LoginPage from "./pages/LoginPage.svelte";
    import CreateAccount from "./pages/CreateAccount.svelte";
    import HomePage from "./pages/HomePage.svelte";
    import DevelopmentPage from "./pages/DevelopmentPage.svelte";
    import AccuntDetails from "./pages/AccountDetails.svelte";
    import { checkLoginStatus, isAuthenticated } from "./stores/stores";
    import { onMount } from "svelte";
    const routes = {
        '/dev' : DevelopmentPage,
        '/' : LoginPage,
        '/CreateAccount' : CreateAccount,
        '/home' : HomePage,
        '/account-details' : AccuntDetails
    };

    let isLoggedIn = false;

    onMount(async () => {
        isLoggedIn = await checkLoginStatus();
        console.log(isLoggedIn);
        if (!isLoggedIn){
            push('/');
        }
    });

     // Protect routes
    function routeGuard(route) {
        console.log('Routing to: ' , route);
        if (route === '/home' || route === '/account-details') {
            if (isLoggedIn) {
                push('/'); // Redirect to the login page if not authenticated
                return false;
            }
        }
        return true;
    }

</script>

<main>
    <Router { routes } { routeGuard } />
</main>
