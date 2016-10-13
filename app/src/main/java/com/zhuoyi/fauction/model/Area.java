package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/5/30.
 */
public class Area {


    /**
     * id : 9500
     * name : 湖南省
     * city : [{"id":"9501","name":"长沙市","area":[{"id":"9501.001","name":"芙蓉区"},{"id":"9501.002","name":"天心区"},{"id":"9501.003","name":"岳麓区"},{"id":"9501.004","name":"开福区"},{"id":"9501.005","name":"雨花区"},{"id":"9501.006","name":"长沙县"},{"id":"9501.007","name":"望城县"},{"id":"9501.008","name":"宁乡县"},{"id":"9501.009","name":"浏阳市"}]},{"id":"9502","name":"株洲市","area":[{"id":"9502.001","name":"荷塘区"},{"id":"9502.002","name":"芦淞区"},{"id":"9502.003","name":"石峰区"},{"id":"9502.004","name":"天元区"},{"id":"9502.005","name":"株洲县"},{"id":"9502.006","name":"攸　县"},{"id":"9502.007","name":"茶陵县"},{"id":"9502.008","name":"炎陵县"},{"id":"9502.009","name":"醴陵市"}]},{"id":"9503","name":"湘潭市","area":[{"id":"9503.001","name":"雨湖区"},{"id":"9503.002","name":"岳塘区"},{"id":"9503.003","name":"湘潭县"},{"id":"9503.004","name":"湘乡市"},{"id":"9503.005","name":"韶山市"}]},{"id":"9504","name":"衡阳市","area":[{"id":"9504.001","name":"珠晖区"},{"id":"9504.002","name":"雁峰区"},{"id":"9504.003","name":"石鼓区"},{"id":"9504.004","name":"蒸湘区"},{"id":"9504.005","name":"南岳区"},{"id":"9504.006","name":"衡阳县"},{"id":"9504.007","name":"衡南县"},{"id":"9504.008","name":"衡山县"},{"id":"9504.009","name":"衡东县"},{"id":"9504.10","name":"祁东县"},{"id":"9504.11","name":"耒阳市"},{"id":"9504.12","name":"常宁市"}]},{"id":"9505","name":"邵阳市","area":[{"id":"9505.001","name":"双清区"},{"id":"9505.002","name":"大祥区"},{"id":"9505.003","name":"北塔区"},{"id":"9505.004","name":"邵东县"},{"id":"9505.005","name":"新邵县"},{"id":"9505.006","name":"邵阳县"},{"id":"9505.007","name":"隆回县"},{"id":"9505.008","name":"洞口县"},{"id":"9505.009","name":"绥宁县"},{"id":"9505.10","name":"新宁县"},{"id":"9505.11","name":"城步苗族自治县"},{"id":"9505.12","name":"武冈市"}]},{"id":"9506","name":"岳阳市","area":[{"id":"9506.001","name":"岳阳楼区"},{"id":"9506.002","name":"云溪区"},{"id":"9506.003","name":"君山区"},{"id":"9506.004","name":"岳阳县"},{"id":"9506.005","name":"华容县"},{"id":"9506.006","name":"湘阴县"},{"id":"9506.007","name":"平江县"},{"id":"9506.008","name":"汨罗市"},{"id":"9506.009","name":"临湘市"}]},{"id":"9507","name":"常德市","area":[{"id":"9507.001","name":"武陵区"},{"id":"9507.002","name":"鼎城区"},{"id":"9507.003","name":"安乡县"},{"id":"9507.004","name":"汉寿县"},{"id":"9507.005","name":"澧　县"},{"id":"9507.006","name":"临澧县"},{"id":"9507.007","name":"桃源县"},{"id":"9507.008","name":"石门县"},{"id":"9507.009","name":"津市市"}]},{"id":"9508","name":"张家界市","area":[{"id":"9508.001","name":"永定区"},{"id":"9508.002","name":"武陵源区"},{"id":"9508.003","name":"慈利县"},{"id":"9508.004","name":"桑植县"}]},{"id":"9509","name":"益阳市","area":[{"id":"9509.001","name":"资阳区"},{"id":"9509.002","name":"赫山区"},{"id":"9509.003","name":"南　县"},{"id":"9509.004","name":"桃江县"},{"id":"9509.005","name":"安化县"},{"id":"9509.006","name":"沅江市"}]},{"id":"9510","name":"郴州市","area":[{"id":"9510.001","name":"北湖区"},{"id":"9510.002","name":"苏仙区"},{"id":"9510.003","name":"桂阳县"},{"id":"9510.004","name":"宜章县"},{"id":"9510.005","name":"永兴县"},{"id":"9510.006","name":"嘉禾县"},{"id":"9510.007","name":"临武县"},{"id":"9510.008","name":"汝城县"},{"id":"9510.009","name":"桂东县"},{"id":"9510.10","name":"安仁县"},{"id":"9510.11","name":"资兴市"}]},{"id":"9511","name":"永州市","area":[{"id":"9511.001","name":"芝山区"},{"id":"9511.002","name":"冷水滩区"},{"id":"9511.003","name":"祁阳县"},{"id":"9511.004","name":"东安县"},{"id":"9511.005","name":"双牌县"},{"id":"9511.006","name":"道　县"},{"id":"9511.007","name":"江永县"},{"id":"9511.008","name":"宁远县"},{"id":"9511.009","name":"蓝山县"},{"id":"9511.10","name":"新田县"},{"id":"9511.11","name":"江华瑶族自治县"}]},{"id":"9512","name":"怀化市","area":[{"id":"9512.001","name":"鹤城区"},{"id":"9512.002","name":"中方县"},{"id":"9512.003","name":"沅陵县"},{"id":"9512.004","name":"辰溪县"},{"id":"9512.005","name":"溆浦县"},{"id":"9512.006","name":"会同县"},{"id":"9512.007","name":"麻阳苗族自治县"},{"id":"9512.008","name":"新晃侗族自治县"},{"id":"9512.009","name":"芷江侗族自治县"},{"id":"9512.10","name":"靖州苗族侗族自治县"},{"id":"9512.11","name":"通道侗族自治县"},{"id":"9512.12","name":"洪江市"}]},{"id":"9513","name":"娄底市","area":[{"id":"9513.001","name":"娄星区"},{"id":"9513.002","name":"双峰县"},{"id":"9513.003","name":"新化县"},{"id":"9513.004","name":"冷水江市"},{"id":"9513.005","name":"涟源市"}]},{"id":"9514","name":"湘西土家族苗族自治州","area":[{"id":"9514.001","name":"吉首市"},{"id":"9514.002","name":"泸溪县"},{"id":"9514.003","name":"凤凰县"},{"id":"9514.004","name":"花垣县"},{"id":"9514.005","name":"保靖县"},{"id":"9514.006","name":"古丈县"},{"id":"9514.007","name":"永顺县"},{"id":"9514.008","name":"龙山县"}]}]
     */

    private ProvinceBean province;

    public ProvinceBean getProvince() {
        return province;
    }

    public void setProvince(ProvinceBean province) {
        this.province = province;
    }

    public static class ProvinceBean {
        private String id;
        private String name;
        /**
         * id : 9501
         * name : 长沙市
         * area : [{"id":"9501.001","name":"芙蓉区"},{"id":"9501.002","name":"天心区"},{"id":"9501.003","name":"岳麓区"},{"id":"9501.004","name":"开福区"},{"id":"9501.005","name":"雨花区"},{"id":"9501.006","name":"长沙县"},{"id":"9501.007","name":"望城县"},{"id":"9501.008","name":"宁乡县"},{"id":"9501.009","name":"浏阳市"}]
         */

        private List<CityBean> city;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CityBean> getCity() {
            return city;
        }

        public void setCity(List<CityBean> city) {
            this.city = city;
        }

        public static class CityBean {
            private String id;
            private String name;
            /**
             * id : 9501.001
             * name : 芙蓉区
             */

            private List<AreaBean> area;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<AreaBean> getArea() {
                return area;
            }

            public void setArea(List<AreaBean> area) {
                this.area = area;
            }

            public static class AreaBean {
                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
