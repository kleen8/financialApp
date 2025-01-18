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
    max-height: 300px; /* Set height as needed */
    overflow-y: auto;
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 1rem;
    margin: 1rem 0;
}

.transaction {
    display: flex;
    justify-content: space-between;
    padding: 0.5rem 0;
    border-bottom: 1px solid #e0e0e0;
}

.transaction:last-child {
    border-bottom: none;
}
</style>

<main>
    {#if error}
        <p style="color: red;">{error}</p>
    {:else if $transactions.length === 0}
        <p>No transactions found.</p>
    {:else}
        <div class="transaction-box">
            {#each $transactions as { amount, timestamp, category, type}}
                <div class="transaction">
                    <div><strong>Amount:</strong> ${amount} </div>
                    <div><strong> Date:</strong> {new Date(timestamp).toLocaleDateString()} </div>
                    <div><strong> Category:</strong> {category} </div>
                    <div><strong> Type:</strong> {type} </div>
                </div>
            {/each}
        </div>
    {/if}
</main>

