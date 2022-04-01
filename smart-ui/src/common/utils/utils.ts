// @ts-ignore
import isPlainObject from 'lodash/isPlainObject'
import { inBrowser } from 'ant-design-vue/es/_util/env'
import triggerEvent from 'ant-design-vue/es/_util/triggerEvent'

const ObjProto = Object.prototype
const toString = ObjProto.toString
const FN_MATCH_REGEXP = /^\s*function (\w+)/
export const hasOwn = ObjProto.hasOwnProperty

/**
 * 判断是否时函数
 * @param value
 */
export const isFunction = (value: any) => {
  return toString.call(value) === '[object Function]'
}

export const withRequired = (type: any) => {
  Object.defineProperty(type, 'isRequired', {
    get: function get() {
      this.required = true;
      return this;
    },
    enumerable: false
  })
}

export const getComponentFromProp = (instance: any, prop: string) => {
  const slotsFunction = instance.$slots && instance.$slots[prop]
  return slotsFunction ? slotsFunction() : instance.$props[prop]
}

export const isFun = function isFun(func: any) {
  return typeof func === 'function'
}

export const noop = function noop(msg: string) {msg}

export const withDefault = (type: any) => {
  Object.defineProperty(type, 'def', {
    value: function value(def: any) {
      if (def === undefined && this['default'] === undefined) {
        this['default'] = undefined
        return this
      }
      if (!isFunction(def) && !validateType(this, def)) {
        // @ts-ignore
        warn(this._vueTypes_name + ' - invalid default value: "' + def + '"', def)
        return this
      }
      this['default'] = isArray(def) || isPlainObject(def) ? function () {
        return def
      } : def

      return this
    },

    enumerable: false,
    writable: false
  });
}

export const getNativeType = function getNativeType(value: any) {
  if (value === null || value === undefined) return null
  const match = value.constructor.toString().match(FN_MATCH_REGEXP)
  return match && match[1]
}

export const toType = (name: string, obj: any) => {
  Object.defineProperty(obj, '_vueTypes_name', {
    enumerable: false,
    writable: false,
    value: name
  })
  withRequired(obj)
  withDefault(obj)
  if (isFunction(obj.validator)) {
    obj.validator = obj.validator!.bind(obj)
  }
  return obj
}

/**
 * 判断是否是array
 */
export const isArray = Array.isArray || function (value) {
  return toString.call(value) === '[object Array]';
}

export const getType = (fn: any) => {
  const type = fn !== null && fn !== undefined ? fn.type ? fn.type : fn : null
  const match = type && type.toString().match(FN_MATCH_REGEXP)
  return match && match[1]
}

/**
 * 判断是否是integer
 */
export const isInteger = Number.isInteger || function (value) {
  return typeof value === 'number' && isFinite(value) && Math.floor(value) === value
}

export const validateType = function (type: any, value: any) {
  // eslint-disable-next-line prefer-rest-params
  const silent = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false

  let typeToCheck = type
  let valid = true
  let expectedType = void 0
  if (!isPlainObject(type)) {
    typeToCheck = { type: type }
  }
  const namePrefix = typeToCheck._vueTypes_name ? typeToCheck._vueTypes_name + ' - ' : ''
  if (hasOwn.call(typeToCheck, 'type') && typeToCheck.type !== null) {
    if (isArray(typeToCheck.type)) {
      valid = typeToCheck.type.some(function (type: any) {
        // @ts-ignore
        return validateType(type, value, true)
      });
      expectedType = typeToCheck.type.map(function (type: any) {
        return getType(type)
      }).join(' or ')
    } else {
      expectedType = getType(typeToCheck)

      if (expectedType === 'Array') {
        valid = isArray(value);
      } else if (expectedType === 'Object') {
        valid = isPlainObject(value);
      } else if (expectedType === 'String' || expectedType === 'Number' || expectedType === 'Boolean' || expectedType === 'Function') {
        valid = getNativeType(value) === expectedType
      } else {
        valid = value instanceof typeToCheck.type
      }
    }
  }

  if (!valid) {
    silent === false && warn(namePrefix + 'value "' + value + '" should be of type "' + expectedType + '"')
    return false
  }

  if (hasOwn.call(typeToCheck, 'validator') && isFunction(typeToCheck.validator)) {
    valid = typeToCheck.validator(value);
    if (!valid && silent === false) warn(namePrefix + 'custom validation failed');
    return valid;
  }
  return valid
}

let warn = noop

if (!import.meta.env.PROD) {
  const hasConsole = typeof console !== 'undefined';
  warn = function warn(msg: string) {
    if (hasConsole) {
      console.warn('[VueTypes warn]: ' + msg);
    }
  };
}

const themeConfig: any = {
  daybreak: 'daybreak',
  '#1890ff': 'daybreak',
  '#F5222D': 'dust',
  '#FA541C': 'volcano',
  '#FAAD14': 'sunset',
  '#13C2C2': 'cyan',
  '#52C41A': 'green',
  '#2F54EB': 'geekblue',
  '#722ED1': 'purple'
}

export function genThemeToString(val: string | null | undefined) {
  return val && themeConfig[val] ? themeConfig[val] : val;
}

const invertKeyValues = function invertKeyValues(obj: any) {
  return Object.keys(obj).reduce(function (acc: any, key) {
    acc[obj[key]] = key;
    return acc;
  }, {});
}
export function genStringToTheme(val: string) {
  const stringConfig = invertKeyValues(themeConfig);
  return val && stringConfig[val] ? stringConfig[val] : val;
}

export { warn, inBrowser, triggerEvent }
