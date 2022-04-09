<template>
    <div id="dashboard">
        <h1>Welcome XXX, here's the dashboard</h1>
        <button v-on:click="logout_user">Logout</button>
    </div>
</template>

<script>
    export default {
        name: 'Dashboard',
        data() {
            return {
                id: "",
                username: "",
                password: ""
            }
        },
        methods: {

            logout_user: function () {

                fetch("/logout")
                    .then(response => {

                        console.log(response)
                        if (response.status == 200) {
                            window.location.href = response.url.split("?")[0];
                        }

                    })
            }
        },
    }
</script>

<style>
</style>