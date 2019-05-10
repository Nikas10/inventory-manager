<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>All pages</h1>
      <b-table small :items="pages" :fields="fields">
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
      },
      pages: [
        {
          id: 0,
          title: "About",
          access: "User",
          link: "/about"
        },
        {
          id: 1,
          title: "Holders",
          access: "Staff",
          link: "/holders/"
        },
        {
          id: 2,
          title: "Items",
          access: "User",
          link: "/items/"
        },
        {
          id: 3,
          title: "Requirements",
          access: "Staff",
          link: "/requirements/"
        },
        {
          id: 4,
          title: "Requisitions",
          access: "User",
          link: "/requisitions/"
        },
        {
          id: 5,
          title: "Roles",
          access: "User",
          link: "/roles/"
        },
        {
          id: 6,
          title: "Signin",
          access: "User",
          link: "/signin"
        },
        {
          id: 7,
          title: "Signup",
          access: "User",
          link: "/Signup"
        },
        {
          id: 8,
          title: "Users",
          access: "Staff",
          link: "/users/"
        }
      ]
    };
  }
};
</script>
