package daily;

import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * Unsafe 破坏枚举
 *
 * @author xiaozhengN 571457082@qq.com
 * @since 2022-10-16 15:24:02
 **/
public class EnumCreator {
    public static void main(String[] args) throws NoSuchFieldException, InstantiationException {
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long nameOffSet = unsafe.objectFieldOffset(Enum.class.getDeclaredField("name"));
        long ordinalOffSet = unsafe.objectFieldOffset(Enum.class.getDeclaredField("ordinal"));
        Sex sex = (Sex) unsafe.allocateInstance(Sex.class);
        unsafe.compareAndSwapObject(sex, nameOffSet, null, "阴阳人");
        unsafe.compareAndSwapObject(sex, ordinalOffSet, 0, 2);
        System.out.println(sex);
    }
}
