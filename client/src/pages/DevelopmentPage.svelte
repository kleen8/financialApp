<script>
    import AddTransaction from '../lib/AddTransaction.svelte';
    import AddAccount from '../lib/AddAccounts.svelte';
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

    <AddAccount />
    <AddTransaction />
    <button type="button" on:click={callHello}>Hello</button>
</main>
