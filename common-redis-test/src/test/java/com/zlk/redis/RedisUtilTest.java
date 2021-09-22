package com.zlk.redis;


import com.zlk.common.redis.util.RedisUtil;
import com.zlk.core.model.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author likuan.zhou
 * @title: IUserServiceTest
 * @projectName common-test
 * @description: redis测试
 * @date 2021/9/15/015 19:37
 */
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
// 注 类和方法必须为public才能运行
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisUtilTest {

    //过期时间
    private final static Long EXPIRE_TIME = 60*60L;
    //kye分割使用:会按文件夹方式分割归类。
    //字符串类型key（key:String）
    private final static String STR_KEY = "str:key";
    private final static String STR_KEY2 = "str:key2";
    //hash(map)类型key（key:Map<item,Object>）,其中map的item通常为Map中对象的主键id.
    private final static String HASH_KEY = "hash:key";
    private final static String HASH_LONG_KEY = "hash:long:key";
    //list链表类型key（key:list||单个对象）
    private final static String LIST_KEY = "list:key";
    //set链表类型key（key:）
    private final static String SET_KEY = "set:key";
    //zset链表类型key（key:）
    private final static String ZSET_KEY = "zset:key";
    private final static Long SIZE = 5L;
    @Autowired
    private RedisUtil redisUtil;
    //================redis-公用方法===================
    @Test
    public void testDel() {
        //删除单个key
        Boolean bl = redisUtil.del(STR_KEY);
        assertNotNull(bl);
    }

    @Test
    public void testDelS() {
        // 批量删除key
        Set<String> keySet  = new HashSet<>();
        keySet.add(STR_KEY);
        keySet.add(STR_KEY2);
        redisUtil.del(keySet);
    }

    //================redis-字符串类型(最常用类型)===================
    // key 对应String字符串，所有操作都要对整个String字符串对象。
    @Test
    public void testSet() {
        //redisUtil.set(STR_KEY2,getUserList(),EXPIRE_TIME);
        redisUtil.set(HASH_KEY,getUserList(),EXPIRE_TIME);
    }

    @Test
    public void testGet() {
        List<UserVO> userVOList = (List<UserVO>)redisUtil.get(STR_KEY);
        assertNotNull(userVOList);
    }

    //================redis-Hash类型（map）===================
    // 以下为 key:map<item,userVO> 。将list转为list.size()个map集合。
    // list<UserVO> ==>Map<"id",UserVO>==>key:map<item,userVO>
    // 适合存对象集合列表，不同于String需要操作整个对象，hash可以对特定一条数据进行操作（单条增删改查）。也可以对整个key进行操作。
    // 等同于java的HashMap
    @Test
    public void testHmSet() {
        // 入整个map
        List<UserVO> userList = getUserList();
        Map<Long, UserVO> userVOMap = userList.stream().collect(Collectors.toMap(UserVO::getId, userVO -> userVO));
        Boolean bl = redisUtil.hmSet(HASH_KEY, userVOMap, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testHmGet() {
        // 获取key对应整个map
        Map<Object, Object> map = redisUtil.hmGet(HASH_KEY);
        Map<Object, UserVO> userVOMap = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> (UserVO) v.getValue()));
        assertNotNull(userVOMap);
    }

    @Test
    public void testHSet() {
        // 对应key,对map<item,value>入一个键值对（存在更新，不存在则新增）
        List<UserVO> userList = getUserList();
        UserVO userVO =  new UserVO();
        userVO.setId(6L);
        userVO.setName("name:"+6);
        userVO.setStatus(1);
        Boolean bl = redisUtil.hSet(HASH_KEY, 6L,userVO, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testHGet() {
        // 对应key,获取map<item,value>的item键值。
        // 如map<5,value>,获取key中item=5的value
        UserVO userVO = (UserVO)redisUtil.hGet(HASH_KEY, 6L);
        assertNotNull(userVO);
    }

    @Test
    public void testHDel() {
        //删除key对应map中某些键值（hash表）
        redisUtil.hDel(HASH_KEY, 5,6);
    }

    @Test
    public void testHIncr() {
        //递增
        Map<Long,Object> map = new HashMap<>();
        map.put(1001L,10);
        redisUtil.hmSet(HASH_LONG_KEY,map,EXPIRE_TIME);
        redisUtil.hIncr(HASH_LONG_KEY, 1001L,6);
        // 结果map.put(1001L,16);
    }

    @Test
    public void testHHasKey() {
        //判断key对应map<item,value>中是否存在item键。
        // 存在未true，不存在为false
        boolean bl = redisUtil.hHasKey(HASH_KEY, 5L);
        assertEquals(true,bl);
    }

    //================redis-List类型（链表）===================
    // 不管是否存在对应值，链表每次新增都会占且只占一个索引位（不管一次插入多条还是一条）。
    // 效果等同于java的linkedList
    // 下标0为开始，1为第二。-1为结束，-2为倒数第二。
    @Test
    public void testLlSet() {
        // 从右开始插入一个list到redis链表(只会整个list占链表一个索引位置，再增加后不包含在当前list中，而是另外占一个索引位置。慎用)
        // 如执行 testLlSet() 两次。则LIST_KEY[0]有六条数据，则LIST_KEY[1]也会生成六条数据
        List<UserVO> userList = getUserList();
        Boolean bl = redisUtil.llSet(LIST_KEY, userList, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testLSet() {
        // 从右开始插入一条数据到redis链表(之前的list占链表一个位置，再增加后不包含在当前list中，自己占一个链表位置。慎用)
        // 例如：之前testLlSet()方法执行后6条数据占链表的0索引位置。而testLSet()执行的一条数据占链表索引1位置。明显不是我们想要的结果。
        UserVO userVO = new UserVO();
        userVO =  new UserVO();
        userVO.setId(7L);
        userVO.setName("name:"+7);
        userVO.setStatus(1);
        Boolean bl = redisUtil.lSet(LIST_KEY, userVO, EXPIRE_TIME);
        assertEquals(true,bl);
    }

    @Test
    public void testLlGet() {
        //获取key对应索引对应的整个list
        //索引  index>=0时， 0 表头，1 第二个元素，依次类推；
        //index<0时，-1，表尾，-2倒数第二个元素，依次类推
        List<UserVO> userList = (List<UserVO>) redisUtil.lGet(LIST_KEY, 0, -1);
        for (int i = 0; i < userList.size(); i++) {
            System.out.println("索引下标："+i+"。索引对应value:"+userList.get(i));
        }
        //执行testLlSet()，执行testLSet()，执行testLlSet()做新增后结果如下：
        //索引下标：0。索引对应value:[UserVO(id=1, name=name:1, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=2, name=name:2, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=3, name=name:3, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=4, name=name:4, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=5, name=name:5, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)]
        //索引下标：1。索引对应value:UserVO(id=7, name=name:7, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)
        //索引下标：2。索引对应value:[UserVO(id=1, name=name:1, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=2, name=name:2, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=3, name=name:3, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=4, name=name:4, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null), UserVO(id=5, name=name:5, status=1, createBy=null, createTime=null, updateBy=null, updateTime=null)]
        assertEquals(3,userList.size());
    }

    @Test
    public void testLGetIndex() {
        //获取key对应索引对应的单个值
        //索引  index>=0时， 0 表头，1 第二个元素，依次类推；
        //index<0时，-1，表尾，-2倒数第二个元素，依次类推
        List<UserVO> userList = (List<UserVO>) redisUtil.lGetIndex(LIST_KEY, 0);
        assertNotNull(userList);
    }

    @Test
    public void testLSize() {
        //获取key对应索引长度
        long size = redisUtil.lSize(LIST_KEY);
        assertEquals(3,size);
    }

    @Test
    public void testLUpdateIndex() {
        //更新key对应索引1下标所有数据（覆盖）。（1下标为第二条）
        List<UserVO> userList = getUserList();
        boolean bl = redisUtil.lUpdateIndex(LIST_KEY, 1, userList);
        assertEquals(true,bl);
    }

    // lRemove
    @Test
    public void testLTrim() {
        //删除链表数据按下标（0为开始，-1为最后）
        // 删除[0,1]两条数据
         redisUtil.lTrim(LIST_KEY, 0, 1);
    }

    //================redis-set类型（无序集合）===================
    //类似java的HashSet，不会存在重复数据
    @Test
    public void testSSet() {
        // 插入SET,类似java的HashSet，不会存在重复数据（只在当前插入批次去重）
        // 以下只会存在一个set
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean = redisUtil.sSet(SET_KEY, userVO, EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean2 = redisUtil.sSet(SET_KEY, userVO, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testSSet2() {
        // 该方法只适合每次都操作整个key对应SET.
        // 插入SET,类似java的HashSet，不会存在重复数据
        // 以下结果set1与set3一样只会存在一个set,且只有1001与1002
        // set2会自己存在一个Set,数据1001,1002,1004
        Set<String> set1 = new HashSet<>();
        set1.add("1001");
        set1.add("1001");
        set1.add("1002");
        Boolean aBoolean = redisUtil.sSet(SET_KEY, set1, EXPIRE_TIME);
        Set<String> set2 = new HashSet<>();
        set2.add("1001");
        set2.add("1003");
        set2.add("1004");
        Boolean aBoolean2 = redisUtil.sSet(SET_KEY, set2, EXPIRE_TIME);
        Set<String> set3 = new HashSet<>();
        set3.add("1001");
        set3.add("1001");
        set3.add("1002");
        Boolean aBoolean3 = redisUtil.sSet(SET_KEY, set3, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testSGet() {
        // 获取key对应set
        Object sGet = redisUtil.sGet(SET_KEY);
        assertNotNull(sGet);
    }

    @Test
    public void testSSize() {
        // 获取key对应set的size
        long aLong = redisUtil.sSize(SET_KEY);
        assertEquals(3L,aLong);
    }

    @Test
    public void testSRemove() {
        // 移除key对应某些set
        Set<String> set = new HashSet<>();
        set.add("1001");
        set.add("1002");
        //移出的整个set(1001,1002)内容。
        long aLong = redisUtil.sRemove(SET_KEY,set);
        assertEquals(3L,aLong);
    }

    //================redis-zset类型（有序集合）===================
    //类似java的HashSet，不会存在重复数据，但是有分值比重。
    // 适合排名数据（如排行榜等热数据）
    @Test
    public void testZsSet() {
        // 插入SET,类似java的HashSet，不会存在重复数据（只在当前插入批次去重）
        // 以下只会存在2个set(后两条存在，第二条会覆盖第一条).且有比分排名
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean = redisUtil.zsSet(ZSET_KEY, userVO, 1,EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Boolean aBoolean2 = redisUtil.zsSet(ZSET_KEY, userVO,2, EXPIRE_TIME);
        userVO = new UserVO();
        userVO.setId(11L);
        userVO.setName("name:"+11L);
        userVO.setStatus(1);
        Boolean aBoolean3 = redisUtil.zsSet(ZSET_KEY, userVO,3, EXPIRE_TIME);
        assertEquals(true,aBoolean);
    }

    @Test
    public void testZsGet() {
        //获取key对应Zset。按分值范围。[1,3]
        Set<UserVO> userVOSet = (Set<UserVO>)redisUtil.zsGet(ZSET_KEY,1,3);
        assertNotNull(userVOSet);
    }

    @Test
    public void testZsSize() {
        //获取key对应Zset长度
        long aLong = redisUtil.zsSize(ZSET_KEY);
        assertEquals(3L,aLong);
    }

    @Test
    public void testZsRemove() {
        //获取key对应Zset长度
        Set<UserVO> set = new HashSet<>();
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        set.add(userVO);
        long aLong = redisUtil.zsRemove(ZSET_KEY,set);
        assertEquals(3L,aLong);
    }

    @Test
    public void testZsGetScore() {
        //获取key对应value的分数
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Double score = redisUtil.zsGetScore(ZSET_KEY, userVO);
        System.out.println(score);
    }

    @Test
    public void testZsIncr() {
        //对key对应value的分数做加减
        UserVO userVO = new UserVO();
        userVO.setId(10L);
        userVO.setName("name:"+10L);
        userVO.setStatus(1);
        Double score = redisUtil.zsIncr(ZSET_KEY, userVO,8L);
        System.out.println(score);
    }


    //================造数据===================
    private static List<UserVO> getUserList(){
        ArrayList<UserVO> arrayList = new ArrayList<>();
        UserVO userVO;
        for (long i = 1L; i <= SIZE; i++) {
            userVO =  new UserVO();
            userVO.setId(i);
            userVO.setName("name:"+i);
            userVO.setStatus(1);
            arrayList.add(userVO);
        }
        return arrayList;
    }


}
