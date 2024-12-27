<script>

  import { push } from 'svelte-spa-router'

  let username = '';
  let password = '';

  function handleLogin(){
      if (username && password) {
          console.log("Logging in: ", username);
          push("/OtherPage");
      } else {
          alert("Please fill in both fields");
      }
  }

  function showPassword(){
      let x = document.getElementById("password");
      if (x.getAttribute("type") === "password"){
          x.setAttribute("type", "text");
      } else {
          x.setAttribute("type", "password");
      }
  }

</script>

<main>

    <p1>Welcome to you finance app</p1>
    <form on:submit|preventDefault={handleLogin} class="login-form">
        <input type="text" placeholder="Username" bind:value={username} />
        <input id="password" type="password" placeholder="Password" bind:value={password} />
        <label>
            <input type="checkbox" on:change={showPassword} /> Show Password
        </label>
        <button type="submit">Login</button>
    </form>

    <p>Dont have an account?</p>
    <button on:click={() => push('/CreateAccount')}>Create One Here</button>

</main>

<style>
.login-form {
    display: flex;
    flex-direction: column; /* Stack elements vertically */
    gap: 10px; /* Space between form elements */
    width: 300px; /* Optional: Set width of the form */
    margin: 0 auto; /* Center the form on the page */
}
</style>
