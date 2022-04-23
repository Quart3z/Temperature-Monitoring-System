<template>
    <div id="main">
        <Sidebar :currentPage="currentPage" @navigate="navigate" />
        <div class="float-end main-panel">
            <Header :id=user.id />
            <Body :id=user.id :currentPage="currentPage" />
        </div>
    </div>
</template>

<script>
    import Sidebar from "./sidebar.vue";
    import Header from "./header.vue";
    import Body from "./body.vue";

    export default {
        name: 'Main',
        data() {
            return {
                user: {},
                currentPage: "Dashboard"
            }
        },
        methods: {
            navigate: function(page){
                this.currentPage = page
            }
        },
        mounted() {

            fetch("/getUser")
                .then(response => {

                    if (response.status == 200) {
                        response.json().then(data => {
                            this.user = data
                        })
                    } else {
                        console.log(response);
                    }
                })

        },
        components: {
            Sidebar,
            Header,
            Body
        }
    }
</script>

<style>
</style>