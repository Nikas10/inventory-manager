<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>All pages</h1>
      <b-table small :items="storage.pages" :fields="fields">
        <template slot="title" slot-scope="data">
          <b-link :to="data.item.link">{{data.value}}</b-link>
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
        title: {
          label: "Page",
          sortable: true
        },
        access: {
          label: "Access",
          sortable: true
        }
      }
    };
  }
};
</script>
