<script>
import { accounts }  from '../stores/stores.js';
import { onMount } from 'svelte';
import AddTransaction from '../lib/AddTransaction.svelte';
import GetTransaction from '../lib/GetTransaction.svelte';
import { push } from 'svelte-spa-router';
import { checkLoginStatus, isAuthenticated, transactions } from "../stores/stores";
import LogoutUser from '../lib/LogoutUser.svelte';

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

async function deleteAccount(){
    const response = await fetch('/api/delete-account', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        credentials: 'include',
    });
    if (response.ok){
        accounts.set([]);
        await push('/home');
    } else {
        console.error("Account could not be deleted");
    }
}

transactions.subscribe((txns) => {
    if (txns.length > 0){
        getBalance();
    }
});

getBalance();

function pushToHomePage() {
    push('/home');
}

</script>

<main>

    <div class="header">

        <div class="homeButton">
            <button type="button" on:click={pushToHomePage} class="go-back-button">Go back</button>
        </div>

        <div class="title">
            <h1>{accountName} {accountType} Account</h1>
        </div>


        <div class="logout-button">
            <LogoutUser  />
        </div>
    </div>

    <p> Balance: $ {balance} </p>
    <GetTransaction />
    <AddTransaction />
    <button class="delete-button" type="button" on:click={deleteAccount} >Delete Account</button>

</main>

<style>

 main {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px;
    }

    .header .title {
        flex-grow: 1;
         text-align: center;
    }

    .header {
        display: flex;
        justify-content: space-between;
        width: 100%;
        top: 0em;
        position: relative;
        margin-bottom: 20px;
    }

    .header button {
        border-radius: 5px;
        justify-content: start;
    }

    .delete-button{
     position: relative;
     margin-top: 5%;
    }

   .header .logout-button {
        left: 100%;
        justify-content: end;
    }

    h1 {
        margin: 20px 0;
    }

</style>
    
