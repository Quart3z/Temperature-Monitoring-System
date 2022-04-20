<template>
    <div id="dashboard" class="">
        <Utilities :id=id />
        <Chart :id=id />
    </div>
</template>

<script>
    import Utilities from "./utilities.vue"
    import Chart from "../dataset/chart.vue"

    export default {
        name: 'Dashboard',
        data() {
            return {}
        },
        props: {
            id: null
        },
        components: {
            Utilities,
            Chart
        },
    }
</script>