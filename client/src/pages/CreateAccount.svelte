<script>

    import { push } from "svelte-spa-router";
    import { writable } from "svelte/store";
    
    const formData = writable({
        firstName: "",
        lastName: "",
        email: "",
        streetName: "",
        zipCode: "",
        houseNumber: "",
        city: "",
        country: "",
        password: "",
    });

    let showRequirements = false;
    let showPassword = false;
    let isEmailValid = true;
    let isPasswordValid = true

    async function  handleAccountCreation(event){
        event.preventDefault();
        let user;
        formData.subscribe(data => (user = data))();

        console.log(JSON.stringify(user));

        if (isFormValid()){
            console.log("Form is valid");
            try {
                const response = await fetch("/api/create-user", {
                    method : "POST",
                    headers : {
                        "Content-Type" : "application/json",
                    },
                    body: JSON.stringify(user),
                });
                const message = await response.text();
                if(!response.ok) {
                    alert(message);
                    return;
                } 
                console.log("account is created");
                push("/");
            } catch (error) {
                console.error("Network or server error:", error);
            }
        } else {
            alert("Correct the login form");
        }
    }

    function togglePasswordRequirements() {
        showRequirements = true;
    }

    function hidePasswordRequirements() {
        showRequirements = false;
    }

    function togglePasswordVisibility() {
        showPassword = !showPassword;
    }

    // Validate the email input and update the border color if invalid
    function validateEmail() {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test($formData.email)) {
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }
    }

    // Validate the password input
    function validatePassword() {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
        if (!passwordRegex.test($formData.password)) {
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
    }

    function passwordBlur(){
        validatePassword();
        hidePasswordRequirements();
    }

    function isStringNotEmpty(input){
        if (input == ""){
            return false;
        } else {
            return true;
        }
    }

    // Check if all form fields are valid and filled
    function isFormValid() {
        return isStringNotEmpty($formData.firstName) &&
                isStringNotEmpty($formData.lastName) &&
                isStringNotEmpty($formData.email) &&
                isStringNotEmpty($formData.country);
    }

    function pushLoginPage(){
        push('/');
    }


</script>

<main>
    <h1>Welcome to account creation</h1>
    <form on:submit|preventDefault={handleAccountCreation} class="account-creation">
        <input type="email" 
        placeholder="Email" 
        bind:value={$formData.email}
        on:blur={validateEmail}
        class:invalid={!isEmailValid}
        required
        />

        <div id="password-box">
            <div id="password-input">
                <input id="password_field"
                type={showPassword ? 'text' : 'password'}
                placeholder="Password"
                bind:value={$formData.password}
                on:focus={togglePasswordRequirements}
                on:blur={passwordBlur}
                class:invalid={!isPasswordValid}
                required
                />
                <label id="checkbox-label">
                    <input id="password-checkbox" type="checkbox" bind:checked={showPassword} />
                    Show password
                </label>
            </div>
            {#if showRequirements}
              <div class="password-popup">
                <p>The password must contain:</p>
                <ul>
                  <li>At least 8 characters</li>
                  <li>One Uppercase letter</li>
                  <li>One Lowercase letter</li>
                  <li>One Symbol</li>
                </ul>
              </div>
            {/if}
          </div>
        <input type="text" placeholder="Name" bind:value={$formData.firstName} required/>
        <input type="text" placeholder="Family name" bind:value={$formData.lastName} required/>
        <input type="text" placeholder="Country" bind:value={$formData.country} required/>
        <input type="text" placeholder="City" bind:value={$formData.city} required/>
        <input type="text" placeholder="Street name" bind:value={$formData.streetName} required/>
        <input type="text" placeholder="House number" bind:value={$formData.houseNumber} required/>
        <input type="text" placeholder="Zipcode" bind:value={$formData.zipCode} required/>
        <button type="submit" 
            disabled={!isFormValid()}
            >Create Account</button>
        <button type="button" on:click={pushLoginPage}>Go back</button>
    </form>



</main>


<style>

    input {
        box-sizing: border-box;
        width: 200px;
        height: 30px;
    }

    button {
        padding: auto;
        width: 200px;
    }

    .account-creation {
        position: relative;
        display: flex;
        flex-direction: column;
        left: 240px;
        gap: 15px;
        margin: 0 auto;

    }

    #password-box {
        position: relative; /* Ensure relative positioning for the popup to be positioned correctly */    
    }

    #password-input {
        position: relative;
        display: flex;
        flex-direction: row;
        
    }

    #password_field {
        width: 200px;
        height: 30px;
    }

    #password-checkbox {
        width: 30px;
        height: 15px;
    }

    #checkbox-label {
        font-size: 0.8rem;
        height: 30px;
    }


    .password-popup {
        position: absolute;
        top: -300%;
        right: 102%;
        width: 200px;
        padding: 10px;
        background-color: #3d3d3d;
        border: 1px solid #919191;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        font-size: 0.9rem;
        z-index: 100;
    }

    .password-popup ul {
        margin: 0;
        padding-left: 10px;
        list-style: disc;
    }
    .password-popup p {
        margin: 0;
        font-weight: bold;
    }

    .invalid {
        border: 2px solid red;
    }

</style>
