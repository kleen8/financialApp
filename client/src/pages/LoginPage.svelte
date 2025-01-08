<script>

  import { push } from 'svelte-spa-router'

  let username = '';
  let password = '';

  async function handleLogin(){
      if (username && password) {
          let loginCredentials = {
            "email" : username,
            "password" : password
          }
          try {
              const response = await fetch("/api/loginUser", {
                  method : "POST",
                  headers : {
                      "Content-Type" : "application/json",
                  },
                  body: JSON.stringify(loginCredentials),
              });
              console.log(response.status);
              const message = await response.text();
              if(response.ok){
                  console.log(message);
                  push("/home");
              } else {
                  alert("Status: " + response.status + " Message: " + message);
              }
          } catch (error) {
              console.error("Network error:", error);
              alert("Unable to connect to the server");
          }
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
