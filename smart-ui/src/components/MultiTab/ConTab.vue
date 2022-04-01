<template>
  <nav class="site-contabs" :style="{height: height + 'px'}">
    <div :style="{height: height + 'px'}" class="contabs-scroll float-left">
      <ul class="nav con-tabs">
        <li
          v-for="(item, i) in dataList"
          :key="'con-tab-item' + i"
          style="width: auto"
          :class="item.key === value ? ['active'] : []">
          <a
            :style="computeItemStyle"
            :title="item.title"
            href="javascript:"
            @click="() => handleClick(item)">
            <span>title</span>
            <i v-if="i !== 0" style="top: 9px" class="icon wb-close-mini" @click.prevent="() => handleCloseMenu(item)" />
          </a>
        </li>
      </ul>
    </div>
    <div :class="show ? ['show'] : []" class="btn-group float-right">
      <a-dropdown v-model:visible="show" :trigger="['click']">
        <button type="button" class="btn btn-default dropdown-toggle btn-outline" :style="{height: (height -1) + 'px' }">
          <CaretDownOutlined />
        </button>
        <template #overlay>
          <a-menu>
            <a-menu-item>abc</a-menu-item>
            <a-menu-item>abc</a-menu-item>
            <a-menu-item>abc</a-menu-item>
            <a-menu-item>abc</a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </nav>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, computed, ref } from 'vue'

import { CaretDownOutlined } from '@ant-design/icons-vue'


/**
 * 下拉菜单
 */
export default defineComponent({
  name: 'ConTab',
  components: {
    CaretDownOutlined
  },
  props: {
    height: {
      type: Number as PropType<number>,
      default: 30
    },
    dataList: {
      type: Array as PropType<Array<any>>,
      default: () => []
    },
    // 激活的菜单
    value: String as PropType<string>
  },
  setup (props) {
    const { height } = toRefs(props)
    const computeItemStyle = computed(() => {
      return {
        'line-height': height.value + 'px',
        height: height.value + 'px'
      }
    })
    const handleClick = (data: any) => {
      console.log(data)
    }
    const handleCloseMenu = (data: any) => {
      console.log(data)
    }
    const show = ref(false)
    return {
      computeItemStyle,
      handleClick,
      handleCloseMenu,
      show
    }
  }
})
</script>

<style lang="less" scoped>
.site-contabs {
  margin-left: 256px;
  position: fixed;
  top: 60px;
  background: #fff;
  left: 0;
  right: 0;
  border-bottom: solid 1px #e4eaec;
  display: block;
  z-index: 100;

  .contabs-scroll {
    overflow: hidden;
    width: calc(100% - 35px);
    position: relative;
    .con-tabs {
      position: absolute;
      >li {
        float: left;
        .active {
          background: #f1f4f5;
        }
      }
    }
  }
  ::v-deep(.show) {
    .btn-default {
      &.dropdown-toggle {
        color: #76838f;
        &:focus, &:hover {
          background-color: #ccd5db;
          border-color: #ccd5db;
        }
      }
    }
  }
  .btn {
    border-radius: 0;
    background: #FFFFFF;
    border: none;
    cursor: pointer;
    .btn-default {
      color: #76838f
    }
  }
}
</style>
