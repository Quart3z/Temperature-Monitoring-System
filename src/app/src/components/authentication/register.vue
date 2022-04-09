<template>
    <div>
        <form class="authentication_card register_card" v-on:submit.prevent="register_user">
            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': !validation(username) && isSubmit}" v-model="username" placeholder="Username">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': !validation(email) && isSubmit}" v-model="email" placeholder="Email">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': !validation(password) && isSubmit}" v-model="password" placeholder="Password" type="password">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': (!validation(passwordRepeat) || !samePassword()) && isSubmit}" v-model="passwordRepeat" placeholder="Confirm password"
                    type="password">
                <div class="invalid-feedback" v-if="!validation(passwordRepeat)">This field is required</div>
                <div class="invalid-feedback" v-else-if="!samePassword()">Passwords do not matched</div>
            </div>

            <span>Already an user?
                <button type="button" v-on:click="$emit('isRegister', false)">Login at here!</button>
            </span>
            <button type="submit" v-on:click="register_user">Register</button>
        </form>
    </div>
</template>

<script>
    export default {
        name: 'Register',
        data() {
            return {
                username: "",
                email: "",
                password: "",
                passwordRepeat: "",

                isSubmit: false
            }
        },
        methods: {

            validation: function (text) {
                if (text == "") {
                    return false;
                } else {
                    return true;
                }

            },
            samePassword: function () {
                if (this.password == this.passwordRepeat) {
                    return true;
                } else {
                    return false;
                }
            },
            register_user: function () {

                this.isSubmit = true;

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username: this.username,
                        password: this.password
                    })
                };

                fetch("/login", request)
                    .then(response => {

                        console.log(response)

                        if (response.status == 200) {
                            window.location.href = response.url;
                        } else {
                            console.log("Error register ....")
                        }
                    })

            }
        }
    }
</script>