<script>
    async function callHello(){
        let response = await fetch("/api/hello");
        let text = await response.text();
        console.log("api call without env: " ,text);
    }
</script>

<main>
    <button type="button" on:click={callHello}>Hello</button>
</main>
