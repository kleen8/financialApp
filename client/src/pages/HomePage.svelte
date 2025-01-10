<script>
    import AddTransaction from '../lib/AddTransaction.svelte';
    import AddAccount from '../lib/AddAccounts.svelte';
    import { push } from "svelte-spa-router";
    async function callHello(){
        let response = await fetch("/api/hello");
        let text = await response.text();
        console.log("api call without env: " ,text);
    }

    async function checkLoginStatus(){
        try {
            const response = await fetch('/api/check-login', {
                method : 'GET',
                credentials : 'include'
                });
            if (response.ok) {
                console.log('User is logged in');
            } else {
                console.log('User is not logged in');
                push('/');
            }
        } catch (error) {
            console.error('Error checking login status: ', error);
            push('/');
        }
    }

    checkLoginStatus();

</script>

<main>
    
    <div id="header">
        <h1>Welcome to Your Financial App</h1>
        <p>Manage your accounts, incomes, and expenses easily.</p>
    </div>

    <AddAccount />
    <AddTransaction />
    <button type="button" on:click={callHello}>Hello</button>
</main>

<style>

   #header{
       position: absolute;
       top: 0q;
   }


</style>
