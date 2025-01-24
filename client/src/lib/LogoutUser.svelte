<script> 
    import { push } from "svelte-spa-router";

    async function handleLogout(){
        try {
            const response = await fetch('/api/logout-user' , {
                method: 'POST',
                headers: {
                    "Content-Type" : 'application/json',
                },
                credentials: 'include',
            });

            if ( response.ok){
                await push('/');
            } else {
                console.error("Failed to log out: " , await response.text());
            }
        } catch (error) {
            console.error("An error ocurred during the logout", error);
        }
    }

</script>

<main>

<button on:click={handleLogout} class="logout-button">Log Out</button>

</main>

<style>
    .logout-button {
        padding: 10px 15px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    .logout-button:hover {
        background-color: #e53e3e;
    }
</style>

