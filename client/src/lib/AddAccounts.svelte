<script>

import { accounts } from "../stores/stores.js";
import { writable } from "svelte/store";

let showModal = false;
let accountType = "General";
let balance = 0;
let accountName = ""

const accountTypes = ["General", "Saving", "Investing"];

const accountData = writable({
    accountName: "",
    accountType: "General",
    balance: 0
});

function resetForm() {
    accountType = "General";
    balance = 0;
    accountName = "";
}

async function createAccount() {
    if ($accountData.balance < 0 || $accountData.accountName === ""){
        alert("Please fill in the fields.");
    } else {
        let account;
        accountData.subscribe(data => (account = data));
        const response = await fetch("/api/create-account", {
            method: "POST",
            headers: {
                "Content-Type" : "application/json",
                },
            body: JSON.stringify(account),
        });
        console.log("account that got send is: ", account);
        const newAccount = await response.json();
        console.log("account updated with: ", newAccount);
        accounts.update(currentAccounts => [...currentAccounts, newAccount]);
        showModal = false;
        resetForm();
    }
}

</script>

<style>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background-color: rgba(0, 0, 0, 4);
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.modal-header h2 {
    margin: 0;
}

.modal-header button {
    background: none;
    border: none;
    font-size: 20px;
    cursor: pointer;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
}

.form-group input,
.form-group select {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
}

.actions {
    display: flex;
    justify-content: flex-end;
}

.actions button {
    margin-left: 10px;
}
</style>

<button on:click={() => (showModal = true)}>Add Account</button>

{#if showModal}
    <div class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Create a New Account</h2>
                <button on:click={() => (showModal = false)}>✖</button>
            </div>

            <div class="form-group">
                <label>Account Type
                    <select bind:value={$accountData.accountType}>
                        {#each accountTypes as type}
                            <option value={type}>{type}</option>
                        {/each}
                    </select>
                </label>
            </div>

            <div class="form-group">
                <label>Account Name
                    <input
                        type="text"
                        bind:value={$accountData.accountName}
                        placeholder="Account name"
                        required
                    />
                </label>
            </div>


            <div class="form-group">
                <label>Initial Balance
                    <input
                        type="number"
                        step="0.01"
                        bind:value={$accountData.balance}
                        placeholder="Enter initial balance"
                        required
                    />
                </label>
            </div>

            <div class="actions">
                <button on:click={() => (showModal = false)}>Cancel</button>
                <button on:click={createAccount}>Create Account</button>
            </div>
        </div>
    </div>
{/if}

