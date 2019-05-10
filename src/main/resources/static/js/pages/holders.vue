<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Holders</h1>
      <b-table small :items="holders" :fields="fields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/holders/' + data.item.id">{{data.value}}</b-link>
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
        }
      },
      holders: []
    };
  },
  methods: {
    loadHolders: function() {
      const self = this;
      this.$server.get("/holder/").then(function(response) {
        self.holders = response.data;
      });
    }
  },
  mounted: function() {
    this.loadHolders();
  }
};
</script>
