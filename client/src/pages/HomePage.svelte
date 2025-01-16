<script>
    import GetAccounts from '../lib/GetAccounts.svelte';
    import AddTransaction from '../lib/AddTransaction.svelte';
    import AddAccount from '../lib/AddAccounts.svelte';
    import { checkLoginStatus, isAuthenticated } from "../stores/stores";
    import { onMount } from 'svelte';
    import { push } from 'svelte-spa-router';

   onMount(async () => {
        let isLoggedIn = await checkLoginStatus();
        console.log("on mount logincheck for home page: " + isLoggedIn);
        isAuthenticated.subscribe((val) => {
            let value = val;
            console.log("isAuthenticated value: " + value);
            if (!val){
                push("/");
            }
            });
    });
    
    async function callHello(){
        let response = await fetch("/api/hello");
        let text = await response.text();
        console.log("api call without env: " ,text);
    }
</script>

<main>
    
    <div id="header">
        <h1>Welcome to Your Financial App</h1>
        <p>Manage your accounts, incomes, and expenses easily.</p>
    </div>

    <GetAccounts />
    <AddAccount />
    <AddTransaction />
    <button type="button" on:click={callHello}>Hello</button>
</main>

<style>
</style>
