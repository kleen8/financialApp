<script>

    import { push } from "svelte-spa-router";
    
    let password = '';
    let name = '';
    let familyName = '';
    let streetName = '';
    let houseNumber = '';
    let city = '';
    let zipcode = '';
    let email_user = '';
    let country = '';

    let showRequirements = false;
    let showPassword = false;
    let isEmailValid = true;
    let isPasswordValid = true

    async function  handleAccountCreation(){
        if (isFormValid()){
            console.log("Form is valid");
            const user = userToJson();

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

    function userToJson(){
        let user = {
            "firstName" : name,
            "familyName" : familyName,
            "streetName" : streetName,
            "houseNumber" : houseNumber,
            "city" : city,
            "zipCode" : zipcode,
            "emailUser" : email_user,
            "country" : country,
            "password" : password
        }
        return user;
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
        if (!emailRegex.test(email_user)) {
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }
    }

    // Validate the password input
    function validatePassword() {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
        if (!passwordRegex.test(password)) {
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
    }

    function passwordBlur(){
        validatePassword();
        hidePasswordRequirements();
    }

    function isStringEmpty(input){
        if (input == ""){
            return false;
        } else {
            return true;
        }
    }

    // Check if all form fields are valid and filled
    function isFormValid() {
        return isStringEmpty(name) && isStringEmpty(familyName);
    }
</script>

<main>
    <h1>Welcome to account creation</h1>
    <form on:submit|preventDefault={handleAccountCreation} class="account-creation">
        <input type="email" 
        placeholder="Email" 
        bind:value={email_user}
        on:blur={validateEmail}
        class:invalid={!isEmailValid}
        required
        />

        <div id="password-box">
            <div id="password-input">
                <input id="password_field"
                type={showPassword ? 'text' : 'password'}
                placeholder="Password"
                bind:value={password}
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
        <input type="text" placeholder="Name" bind:value={name} required/>
        <input type="text" placeholder="Family name" bind:value={familyName} required/>
        <input type="text" placeholder="Country" bind:value={country} required/>
        <input type="text" placeholder="City" bind:value={city} required/>
        <input type="text" placeholder="Street name" bind:value={streetName} required/>
        <input type="text" placeholder="House number" bind:value={houseNumber} required/>
        <input type="text" placeholder="Zipcode" bind:value={zipcode} required/>
        <button type="submit" 
            disabled={!isFormValid()}
            >Create Account</button>
    </form>
</main>


<style>

    input {
        box-sizing: border-box;
        width: 200px;
        height: 30px;
    }

    button {
        width: 200px;
    }

    .account-creation {
        position: relative;
        display: flex;
        flex-direction: column;
        left: 200px;
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
