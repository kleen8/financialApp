<script>
import { onMount } from 'svelte';
import { accounts, triggerFetchAccounts }  from '../stores/stores.js';
import { push } from 'svelte-spa-router';

let accountsList = [];
let error = null;
const fetchAccounts = async () => {
    try {
        const response = await fetch('/api/get-accounts')
        if (!response.ok){
            throw new Error('Error: ' + response.statusText);
        }
        const data = await response.json();
        console.log(JSON.stringify(data));
        accounts.set(data);
        accountsList = data;
    } catch (err){
        error = err.message;
        console.error(err);
    }
};

triggerFetchAccounts.subscribe(($triggerFetchAccounts) => {
    if($triggerFetchAccounts) {
        console.log("Triggerd by store");
        fetchAccounts();
        triggerFetchAccounts.set(false);
        }
});

onMount(fetchAccounts);

function handleButtonClick(account) {
    // TODO : go to selected account, so push the user to a new page with the account details
    const queryParams = new URLSearchParams({
        name: account.accountName,
        type: account.accountType,
    }).toString();
    console.log(queryParams);
    console.log("Constructed URL:", `/account-details?${queryParams}`);
    console.log("Go to account: " + account.accountName);
    push('/account-details?' + queryParams);
}


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
    font-weight: bold;
}
</style>

{#if error}
    <p class="error">{error}</p>
{:else if accountsList.length === 0}
    <p class="error">No accounts found. Create a new one!</p>
{:else}
    <!-- Render Accounts List -->
    <ul class="account-list">
        {#each accountsList as account}
            <li class="account-item">
                <h3>{account.accountName}</h3>
                <p>Type: {account.accountType}</p>
                <p>Balance: ${account.balance.toFixed(2)}</p>
                <button on:click={() => handleButtonClick(account)}>View details</button>
            </li>
        {/each}
    </ul>
{/if}
