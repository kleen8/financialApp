<script>
import { onMount } from 'svelte';
import AddTransaction from '../lib/AddTransaction.svelte';
import GetTransaction from '../lib/GetTransaction.svelte';
import { push } from 'svelte-spa-router';
import { checkLoginStatus, isAuthenticated, transactions } from "../stores/stores";
let accountName = '';
let accountType = '';
let balance = 0.0;
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


async function getBalance(){
    const response = await fetch("/api/get-account-balance");
    const accountBalance = await response.json();
    console.log(accountBalance);
    balance = accountBalance;
}

transactions.subscribe((txns) => {
    if (txns.length > 0){
        getBalance();
    }
});

getBalance();

</script>

<main>
    <h1>{accountName} {accountType} Account</h1>
    <p> Balance: $ {balance} </p>
    <GetTransaction />
    <AddTransaction />


</main>

