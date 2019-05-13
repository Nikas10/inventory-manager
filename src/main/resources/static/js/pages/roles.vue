<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Roles</h1>
      <b-table small :items="roles" :fields="fields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/roles/' + data.item.id">{{data.value}}</b-link>
        </template>
      </b-table>
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
        },
        description: {
          label: "Description",
          sortable: true
        },
      },
      roles: []
    };
  },
  methods: {
    loadRoles: function() {
      const self = this;
      this.$server.get("/roles/").then(function(response) {
        self.roles = response.data;
      });
    }
  },
  mounted: function() {
    this.loadRoles();
  }
};
</script>
