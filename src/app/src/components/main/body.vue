<template>
    <div id="body">
        <Dashboard :id=id v-if="currentPage === 'Dashboard'" />
        <Dataset :id=id v-if="currentPage === 'Dataset'" />
    </div>
</template>

<script>
    import Dashboard from "./dashboard/dashboard.vue"
    import Dataset from "./dataset/dataset.vue"

    export default {
        name: 'Body',
        props: {
            id: null,
            currentPage: null
        },
        components: {
            Dashboard,
            Dataset
        },
    }
</script>