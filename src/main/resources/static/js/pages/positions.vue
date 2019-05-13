<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Positions</h1>
      <b-table small :items="positions" :fields="fields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/positions/' + data.item.id">{{data.value}}</b-link>
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
        bundle: {
          label: "Bundle",
          sortable: true
        }
      },
      positions: []
    };
  },
  methods: {
    loadPositions: function() {
      const self = this;
      this.$server.get("/positions/").then(function(response) {
        self.positions = response.data;
      });
    }
  },
  mounted: function() {
    this.loadPositions();
  }
};
</script>
