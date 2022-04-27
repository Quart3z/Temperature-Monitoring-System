<template>
    <div id="sidebar" class="position-fixed sidebar bg-dark p-3">
        <div class="text-center">
            <h1>[LOGO]</h1>
        </div>
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item"><a v-bind:class="{'text-white':true, 'nav-link':true, 'active': currentPage === 'Dashboard'}" v-on:click="navigator('Dashboard')">Dashboard</a></li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: 'Sidebar',
        data() {
            return {}
        },
        props: {
            currentPage: null
        },
        methods: {
            navigator: function (page) {
                this.$emit("navigate", page)
            }
        },
    }
</script>