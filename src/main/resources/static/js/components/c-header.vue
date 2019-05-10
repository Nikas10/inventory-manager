<template>
  <b-navbar class="c-header" toggleable="lg" type="light" variant="light">
    {{userLogin}} | {{storage}}
    <b-navbar-brand to="start">Inventory Mananager</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item v-for="page in pages" v-bind:key="page.id" v-bind:to="page.link">{{page.title}}</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto" v-if="storage && storage.user">
        <b-nav-item-dropdown right>
          <template slot="button-content">
            <em>{{userLogin}}</em>
          </template>
          <b-dropdown-item :to="profilePath">Profile</b-dropdown-item>
          <b-dropdown-item @click="onLogOut">Log Out</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto" v-else>
        <b-nav-item to="signin">Sign In</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
module.exports = {
  props: ["storage"],

  data: function() {
    return {
      userLogin: "",
      pages: [
        {
          id: 0,
          title: "Requests",
          link: "requests"
        },
        {
          id: 1,
          title: "Equipment",
          link: "equipment"
        },
        {
          id: 2,
          title: "About",
          link: "about"
        }
      ]
    };
  },
  computed: {
    profilePath: function() {
      return "/users/" + this.userLogin;
    }
  },
  methods: {
    onLogOut: function() {
      this.$root.logout();
      // TODO Добавить перенаправление на стартовую страницу
    },
    loadHeaderComponent: function() {
      console.log(this.storage);
      // Clone
      if (this.storage && this.storage.user && this.storage.user.login) {
        this.userLogin = this.storage.user.login;
      } else {
        this.userLogin = "";
      }
    }
  },
  mounted() {
    this.loadHeaderComponent();
  },
  watch: {
    "storage.user": function(a, b) {
      this.loadHeaderComponent();
    }
  }
};
</script>

<style>
.c-header {
  margin-bottom: 15pt;
}
</style>
