<script>
    let showModal = false;
    let transactionType = "Income";
    let amount = 0;
    let category = "";
    let recurrent = false;
    let timeInterval = "Weekly";
    let timestamp = new Date().toISOString().slice(0,16);

    const timeIntervals = ["Dayly","Weekly" , "Monthly" , "Yearly" ];

    let transaction = {
        type : transactionType,
        amount : amount,
        catergory : category.trim(),
        recurrent,
        timeInterval: recurrent ? timeInterval : null,
        timestamp: new Date(timestamp),
    };

    function resetForm() {
        amount = 0;
        category = "";
        recurrent = false;
        timeInterval = "WEEKS";
        timestamp = new Date().toISOString().slice(0,16);
    }

    function addTransaction() {
        if (!amount || isNaN(amount) || amount <= 0){
            alert("Please enter a valid amount.");
            return;
        }
        console.log("transaction added:", transaction);
        showModal = false;
        resetForm();
    }
    
    function updateAmount(){
        console.log(amount);
    }

    function updateTransaction(){
        transaction = {
            type : transactionType,
            amount : amount,
            catergory : category.trim(),
            recurrent,
            timeInterval: recurrent ? timeInterval : null,
            timestamp: new Date(timestamp),
        };
        addTransaction();
    };

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

  .form-group input[type="checkbox"] {
    width: auto;
  }

  .actions {
    display: flex;
    justify-content: flex-end;
  }

  .actions button {
    margin-left: 10px;
  }
</style>

<button on:click={() => (showModal = true)}>Add Transaction</button>

{#if showModal}
  <div class="modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Add Transaction</h2>
        <button on:click={() => (showModal = false)}>✖</button>
      </div>

      <div class="form-group">
        <label>Transaction Type
        <select bind:value={transactionType}>
          <option value="Income">Income</option>
          <option value="Expense">Expense</option>
        </select>
        </label>
      </div>

      <div class="form-group">
        <label>Amount
        <input type="number" step="0.01" bind:value={amount} on:change={updateAmount} placeholder="Enter amount" /></label>
      </div>

      <div class="form-group">
        <label>Category
        <input type="text" bind:value={category} placeholder="Enter category (e.g., Rent, Salary)" /></label>
      </div>

      <div class="form-group">
        <label>Timestamp
        <input type="datetime-local" bind:value={timestamp} /></label>
      </div>

      <div class="form-group">
        <label>
          <input type="checkbox" bind:checked={recurrent} />
          Recurrent
        </label>
      </div>

      {#if recurrent}
        <div class="form-group">
          <label>Time Interval
          <select bind:value={timeInterval}>
            {#each timeIntervals as interval}
              <option value={interval}>{interval}</option>
            {/each}
          </select>
          </label>
        </div>
      {/if}

      <div class="actions">
        <button on:click={() => (showModal = false)}>Cancel</button>
        <button on:click={updateTransaction}>Add Transaction</button>
      </div>
    </div>
  </div>
{/if}
