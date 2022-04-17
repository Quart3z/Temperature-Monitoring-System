<template>
    <div id="dashboard" class="">
        <Selection :id=id />
    </div>
</template>

<script>
    import Selection from "./selection.vue"

    export default {
        name: 'Dashboard',
        data() {
            return {}
        },
        props: {
            id: null
        },
        components: {
            Selection
        },
    }
</script>