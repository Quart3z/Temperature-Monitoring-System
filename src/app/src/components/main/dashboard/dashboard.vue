<template>
    <div id="dashboard" class="">
        <Chart :id=id />
        <ML :id=id />
    </div>
</template>

<script>
    import Chart from "./chart/chart.vue"
    import ML from "./chart/machineLearning.vue"

    export default {
        name: 'Dashboard',
        data() {
            return {}
        },
        props: {
            id: null
        },
        components: {
            Chart,
            ML
        },
    }
</script>