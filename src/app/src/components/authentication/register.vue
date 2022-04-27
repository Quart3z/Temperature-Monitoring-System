<template>
    <div id="register" class="authentication-card p-3">
        <div v-if="!doneRegistration">
            <div class="mb-5">
                <h3 class="text-primary">Registration</h3>
            </div>
            <form v-on:submit.prevent="register_user">
                <div class="form-group mb-3">
                    <label for="register_username" class="form-label">Username</label>
                    <input id="register_username" v-bind:class="{'form-control':true, 'is-invalid': (!validation(username) || invalidCredential) && isSubmit}" v-model="username"
                        placeholder="Username">
                    <div class="invalid-feedback" v-if="!validation(username)">This field is required</div>
                    <div class="invalid-feedback" v-else-if="invalidCredential">Username is taken</div>
                </div>

                <div class="form-group mb-3">
                    <label for="register_email" class="form-label">Email</label>
                    <input id="register_email" v-bind:class="{'form-control':true, 'is-invalid': (!validation(email) || !validEmail()) && isSubmit}" v-model="email" placeholder="Email">
                    <div class="invalid-feedback" v-if="!validation(email)">This field is required</div>
                    <div class="invalid-feedback" v-else-if="!validEmail()">Invalid email</div>
                </div>

                <div class="form-group mb-3">
                    <label for="register_password" class="form-label">Password</label>
                    <input id="register_password" v-bind:class="{'form-control':true, 'is-invalid': !validation(password) && isSubmit}" v-model="password" placeholder="Password" type="password">
                    <div class="invalid-feedback">This field is required</div>
                </div>

                <div class="form-group mb-5">
                    <label for="register_cPassword" class="form-label">Confirm password</label>
                    <input id="register_cPassword" v-bind:class="{'form-control':true, 'is-invalid': (!validation(passwordRepeat) || !samePassword()) && isSubmit}" v-model="passwordRepeat"
                        placeholder="Confirm password" type="password">
                    <div class="invalid-feedback" v-if="!validation(passwordRepeat)">This field is required</div>
                    <div class="invalid-feedback" v-else-if="!samePassword()">Passwords do not matched</div>
                </div>

                <div class="mb-3 d-grid">
                    <button class="btn btn-primary" type="submit">Register</button>
                </div>
            </form>

            <hr>

            <div class="my-4">
                <span class="fw-normal me-2">Already a user?</span><a class="link-secondary" v-on:click="$emit('isRegister', false)">Login at here!</a>
            </div>

        </div>

        <Confirmation v-else />
    </div>
</template>

<script>
    import Confirmation from "./confirmation.vue";

    export default {

        name: 'Register',
        data() {
            return {
                username: "",
                email: "",
                password: "",
                passwordRepeat: "",
                isSubmit: false,
                doneRegistration: false,
                invalidCredential: false
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
            validEmail: function () {

                var expression =
                    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

                if (expression.test(this.email)) {
                    return true;
                } else {
                    return false;
                }
            },
            register_user: function () {

                this.isSubmit = true;

                if (this.username != "" && this.email != "" && this.password != "" && this.passwordRepeat != "") {


                    const request = {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            username: this.username,
                            email: this.email,
                            password: this.password
                        })
                    };

                    fetch("/register", request)
                        .then(response => {
                            console.log(response)
                            if (response.status == 200) {
                                this.doneRegistration = true;
                                this.invalidCredential = false;
                            } else {
                                this.invalidCredential = true;
                            }
                        })

                }
            }
        },
        components: {
            Confirmation
        }
    }
</script>