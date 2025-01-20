<script>
import { accounts }  from '../stores/stores.js';
import { push } from 'svelte-spa-router';

let error = null;



const fetchAccounts = async () => {
    try {
        const response = await fetch('/api/get-accounts')
        if (!response.ok){
            throw new Error('Error: ' + response.statusText);
        }
        const data = await response.json();
        accounts.set(data);
        console.log("In getAccounts, accounts are: " , $accounts);
    } catch (err){
        error = err.message;
        console.error(err);
    }
};

fetchAccounts();

async function handleButtonClick(account) {
    console.log(account);
    const queryParams = new URLSearchParams({
        name: account.accountName,
        type: account.accountType,
    }).toString();
    const response = await fetch('/api/post-account-credentials', {
            method: "POST",
            headers: {
                "Content-Type" : "application/json",
                },
                body: JSON.stringify(account),
    });
    if (response.ok){
        push('/account-details?' + queryParams);
    }
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

{#if $accounts.length === 0}
    <p class="error">No accounts found. Create a new one!</p>
{:else}
    <!-- Render Accounts List -->
    <ul class="account-list">
        {#each $accounts as account }
            <li class="account-item">
                <h3>{account.accountName}</h3>
                <p>Type: {account.accountType}</p>
                <p>Balance: ${account.balance} </p>
                <button on:click={() => handleButtonClick(account)}>View details</button>
            </li>
        {/each}
    </ul>
{/if}
