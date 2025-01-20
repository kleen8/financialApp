<script>
import { onMount } from 'svelte';
import AddTransaction from '../lib/AddTransaction.svelte';
import GetTransaction from '../lib/GetTransaction.svelte';
import { push } from 'svelte-spa-router';
import { checkLoginStatus, isAuthenticated } from "../stores/stores";
let accountName = '';
let accountType = '';

onMount(async () => {
    const hash = window.location.hash;
    const queryParams = new URLSearchParams(hash.split('?')[1] || ''); // Extract the part after '?'
    let isLoggedIn = await checkLoginStatus();
    console.log(queryParams);
    console.log("on mount logincheck in account details: " + isLoggedIn);
    isAuthenticated.subscribe((val) => {
        if (!val){
            push("/");
        }
    });
    accountName = queryParams.get('name');
    accountType = queryParams.get('type');
});


</script>

<main>
    <h1>{accountName} {accountType} Account</h1>
    <GetTransaction />
    <AddTransaction />


</main>

