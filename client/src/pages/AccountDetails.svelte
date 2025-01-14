<script>
import { onMount } from 'svelte';

let accountName = '';
let accountType = '';

onMount(() => {
    const queryParams = new URLSearchParams(window.location.search);
    accountName = queryParams.get('name');
    accountType = queryParams.get('type');
});
</script>

<main>
    <h1>Account Details</h1>
    <p><strong>Account Name:</strong> {accountName}</p>
    <p><strong>Account Type:</strong> {accountType}</p>
</main>

