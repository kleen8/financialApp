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
            //push("/");
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
        //await push('/home');
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
    //push('/home');
}

</script>

<main>

    <div class="header">

        <button type="button" on:click={pushToHomePage} class="go-back-button">Go back</button>
        
        <div class="title">
            <h1>{accountName} {accountType} Account</h1>
        </div>


        <div class="logout">
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
        width: 100%;
        flex-direction: column;
        align-items: center;
        padding: 20px;
    }

    .header {
        display: flex;
        justify-content: space-between; /* Ensure items take the full width */
        align-items: center;
        width: 100%; /* Full width */
        margin-bottom: 20px;
        padding: 10px 20px; /* Add some padding for spacing */
        box-sizing: border-box; /* Include padding in the width */
    }

    .go-back-button {
        justify-content: start;
        color: white;
        padding: 10px 15px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .go-back-button:hover {
        background-color: #2b6cb0;
    }

    .title {
        flex-grow: 1; /* Allow the title to take up available space */
        text-align: center; /* Center the text in the title */
    }

    .logout {
        display: flex;
        justify-content: end;
    }

    .delete-button {
        background-color: #f56565;
        color: white;
        padding: 10px 15px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        margin-top: 20px;
    }

    .delete-button:hover {
        background-color: #e53e3e;
    }

    h1 {
        margin: 0; /* Remove unnecessary margin for better alignment */
    }
</style>
