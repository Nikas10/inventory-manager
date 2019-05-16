<template>
  <c-default-page :storage="storage">
    <b-container well>
      <h1>{{'Report' + (storage.user ? ' for user' + storage.user.login : "")}}</h1>
      <b-card>
        <div id="rep"></div>
      </b-card>
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
    return {};
  },
  methods: {
    loadReport: function() {
      if (!this.storage.user) {
        return;
      }
      
      this.$server.get("report").then(function(response) {
        document.getElementById("rep").innerHTML = response.data;
      });
    }
  },
  mounted: function() {
    this.loadReport();
  }
};
</script>
