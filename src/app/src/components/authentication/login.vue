<template>
    <div>
        <form class="authentication_card login_card" v-on:submit.prevent="login_user">
            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': !validation(username) && isSubmit}" v-model="username" placeholder="Username">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <div class="form-group">
                <input v-bind:class="{'form-control':true, 'is-invalid': !validation(password) && isSubmit}" v-model="password" placeholder="Password" type="password">
                <div class="invalid-feedback">This field is required</div>
            </div>

            <span>New user?
                <button type="button" v-on:click="$emit('isRegister', true)">Registers at here!</button>
            </span>
            <button type="submit">Enter</button>
        </form>
    </div>
</template>

<script>
    export default {
        name: 'Login',
        data() {
            return {
                isSubmit: false,
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
                            console.log("Error Sign in in ...")
                        }
                    })

            }
        },
    }
</script>