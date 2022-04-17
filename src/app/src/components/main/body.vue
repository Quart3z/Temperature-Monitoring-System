<template>
    <div id="body">
        <Dashboard :id=id />
    </div>
</template>

<script>
    import Dashboard from "./dashboard/dashboard.vue"


    export default {
        name: 'Body',
        props: {
            id: null
        },
        components: {
            Dashboard,
        },
    }
</script>