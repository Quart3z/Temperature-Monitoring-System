<template>
    <div id="login" class="authentication-card p-3">
        <div class="mb-5">
            <h3 class="text-primary">Login</h3>
        </div>
        <form v-on:submit.prevent="login_user">
            <div class="form-group mb-3">
                <label for="login_username" class="form-label">Username</label>
                <input id="login_username" v-bind:class="{'form-control':true, 'is-invalid': !validation(username) && isSubmit}" v-model="username" placeholder="Username">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group mb-3">
                <label for="login_password" class="form-label">Password</label>
                <input id="login_password" v-bind:class="{'form-control':true, 'is-invalid': !validation(password) && isSubmit}" v-model="password" placeholder="Password" type="password">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group mb-4">
                <div v-bind:style="{display: wrongCredentials}" class="invalid-feedback">Incorrect credentials or account not activated</div>
            </div>

            <div class="mb-3 d-grid">
                <button class="btn btn-primary" type="submit">Login</button>
            </div>
        </form>

        <hr>

        <div class="my-4">
            <span class="fw-normal me-2">New user?</span><a class="link-secondary" v-on:click="$emit('isRegister', true)">Register at here!</a>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'Login',
        data() {
            return {
                isSubmit: false,
                wrongCredentials: "none",
                username: "",
                password: ""
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
            login_user: function () {

                this.isSubmit = true;

                if (this.username != "" && this.password != "") {

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

                            if (response.status == 200) {
                                window.location.href = response.url;
                                this.wrongCredentials = "none";
                            } else {
                                this.wrongCredentials = "block";
                            }
                        })

                }

            }
        },
    }
</script>