<script>
import { onMount } from 'svelte';
import { transactions } from '../stores/stores';

let error = null;

onMount(async () => {
    fetchTransactions();
});

const fetchTransactions = async () =>  {
    try {
        const response = await fetch('/api/get-all-transactions'); // Replace with your actual endpoint
        if (!response.ok) {
            throw new Error('Failed to fetch transactions');
        }
        const fetchedTransactions = await response.json();
        transactions.set(fetchedTransactions);
    } catch (err) {
        error = err.message;
        console.error(err);
    }
};

</script>

<style>

.transaction-box {
    max-height: 300px; /* Limit height if necessary */
    overflow-y: auto; /* Enable vertical scrolling for long content */
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 1rem;
    margin: 1rem 0;
    font-family: Arial, sans-serif;
    font-size: 0.9rem;
}

.transaction-header, .transaction {
    display: grid; /* Use grid for consistent alignment */
    grid-template-columns: 1fr 1fr 1fr 1fr; /* Four equal columns */
    gap: 1rem; /* Space between columns */
    padding: 0.5rem 0;
}

.transaction-header {
    text-align: left;
    font-weight: bold;
    border-bottom: 2px solid #ccc; /* Distinguish the header from rows */
    margin-bottom: 0.5rem;
}

.transaction {
    border-bottom: 1px solid #e0e0e0;
}

.transaction:last-child {
    border-bottom: none; /* Remove border for the last transaction */
}

.transaction div {
    text-align: left; /* Align text to the left for clarity */
    white-space: nowrap; /* Prevent wrapping of text */
    overflow: hidden; /* Hide overflowed text */
    text-overflow: ellipsis; /* Add ellipsis for long text */
}

</style>

<main>
    {#if error}
        <p style="color: red;">{error}</p>
    {:else if $transactions.length === 0}
        <p>No transactions found.</p>
    {:else}
        <div class="transaction-box">
            <div class="transaction-header">
                <div>Amount:</div>
                <div>Date:</div>
                <div>Category:</div>
                <div>Type:</div>
            </div>

            {#each $transactions as { amount, timestamp, category, type}}
                <div class="transaction">
                    <div>${amount}</div>
                    <div>{new Date(timestamp).toLocaleDateString()} </div>
                    <div>{category} </div>
                    <div>{type} </div>
                </div>
            {/each}
        </div>
    {/if}
</main>

