<script>
    import GetAccounts from '../lib/GetAccounts.svelte';
    import AddAccount from '../lib/AddAccounts.svelte';
    import LogoutUser from  '../lib/LogoutUser.svelte';
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
    
</script>

<main>

    <div id="header">
        <h1>Welcome to Your Financial App</h1>
        <p>Manage your accounts, incomes, and expenses easily.</p>
    </div>

    <div class="accounts-display">
        <GetAccounts />
    </div>
    
    <div class="add-account-button">
        <AddAccount />
    </div>

    <div class="logout-button">
        <LogoutUser />
    </div>

</main>

<style>

.accounts-display{
    display: inline-block;
    width: 40rem;
}

.logout-button {
    position: relative;
    margin-top: 5%;
}

</style>
