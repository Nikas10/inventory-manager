<template>
  <c-default-page :storage="storage">
    <h1>Requisition</h1>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {
      requisition: {}
    };
  },
  methods: {
    loadPage: function() {
      if (this.$route.params.id == "new") {
      } else {
        this.loadRequisition();
      }
    },
    loadRequisition: function() {
      const self = this;
      const requisitonId = $route.params.id;
      this.$server.get("/requisiton/" + requisitonId).then(function(response) {
        self.requisition = response.data;
      });
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    $route: function(to, from) {
      this.loadPage();
    }
  }
};
</script>
