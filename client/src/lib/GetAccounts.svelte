<script>
   import { onMount } from 'svelte';

   let accounts = [];
   let error = null;

   const fetchAccounts = async () => {
        try {
           const response = await fetch('/api/get-accounts')
            if (!response.ok){
               throw new Error('Error: ' + response.statusText);
           }
            accounts = await response.json();
       } catch (err){
        error = err.message;
        console.error(err);
        }
   };
    onMount(fetchAccounts);
</script>


<style>
    .account-list {
        margin: 20px;
        padding: 0;
        list-style: none;
    }
    .account-item {
        border: 1px solid #ddd;
        padding: 10px;
        margin: 10px 0;
        border-radius: 5px;
        box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
    }
    .account-item h3 {
        margin: 0;
    }
    .account-item p {
        margin: 5px 0;
    }
    .error {
        color: red;
        font-weight: bold;
    }
</style>



{#if error}
    <p class="error">Failed to load accounts: {error}</p>
{:else}

    <!-- Render Accounts List -->
    <ul class="account-list">
        {#each accounts as account}
            <li class="account-item">
                <h3>{account.accountName}</h3>
                <p>Type: {account.accountType}</p>
                <p>Balance: ${account.balance.toFixed(2)}</p>
            </li>
        {/each}
    </ul>
{/if}

