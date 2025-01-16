<script>
import { onMount } from 'svelte';
import { push } from 'svelte-spa-router';
import { checkLoginStatus, isAuthenticated } from "../stores/stores";
let accountName = '';
let accountType = '';

onMount(async () => {
    let isLoggedIn = await checkLoginStatus();
    console.log("on mount logincheck in account details: " + isLoggedIn);
    isAuthenticated.subscribe((val) => {
        let value = val;
        console.log("isAuthenticated value: " + value);
        if (!val){
            push("/");
        }
    });
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

