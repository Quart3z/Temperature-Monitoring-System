<template>
    <div class="container authentication-container">
        <Login v-show="!registration" @isRegister="(n) => registration = n"/>
        <Register v-show="registration" @isRegister="(n) => registration = n"/>
    </div>
</template>

<script>
    import Login from './login.vue'
    import Register from './register.vue'

    export default {
        name: 'Authentication',
        data() {
            return {
                registration: false,
                username: "",
                password: ""
            }
        },
        methods: {
        },
        components: {
            Login,
            Register
        }
    }
</script>