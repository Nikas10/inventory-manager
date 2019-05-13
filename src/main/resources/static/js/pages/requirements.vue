<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requirements</h1>
      <b-table small :items="requirements" :fields="fields"></b-table>
    </b-container>
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
      fields: {
        name: {
          label: "Name",
          sortable: true
        }
      },
      requirements: []
    };
  },
  methods: {
    loadRequirements: function() {
      const self = this;
      this.$server.get("/requirements/").then(function(response) {
        self.requirements = response.data;
      });
    }
  },
  mounted: function() {
    this.loadRequirements();
  }
};
</script>
