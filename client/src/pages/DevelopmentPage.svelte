<script>
  import { onMount, onDestroy } from "svelte";
  import { Chart, registerables } from "chart.js";
    import { transactions } from "../stores/stores";

  Chart.register(...registerables);

  let canvas;
  let chart;

  // Example data - Replace this with actual data
   let balances = [1000, 1200, 1400, 1600, 1800]; // Balance values
   let dates = ["Jan", "Feb", "Mar", "Apr", "May"]; // Corresponding X-axis labels


   onMount(async () => {
    // Destroy any existing chart to avoid duplication
    if (chart) chart.destroy();
    // Create a new chart
    chart = new Chart(canvas, {
      type: "line",
      data: {
        labels: dates, // X-axis labels
        datasets: [
          {
            label: "Account Balance Over Time",
            data: balances, // Y-axis values
            borderColor: "#3182ce",
            backgroundColor: "rgba(49, 130, 206, 0.5)",
            tension: 0.4,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: true },
        },
        scales: {
          x: {
            title: {
              display: true,
              text: "Time",
              font: { size: 14 },
            },
          },
          y: {
            title: {
              display: true,
              text: "Balance ($)",
              font: { size: 14 },
            },
          },
        },
      },
    });
  });

  async function getBalanceData(){
      const response = await fetch('/api/get-all-transactions') 
      const fetchedTransactions = await response.json();
      console.log("fetched transactions are: " , fetchedTransactions);
      transactions.set(fetchedTransactions)

    }

  console.log("transactions are: " , transactions);
  // Cleanup the chart instance when the component is destroyed
  onDestroy(() => {
    if (chart) chart.destroy();
  });

</script>

<canvas bind:this={canvas}></canvas>

<style>
  canvas {
    max-width: 100%;
    height: auto;
    margin: 20px 0;
  }
</style>
